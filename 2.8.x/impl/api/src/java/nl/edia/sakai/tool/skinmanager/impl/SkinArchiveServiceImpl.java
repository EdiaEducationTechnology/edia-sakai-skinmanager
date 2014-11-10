/*
 * #%L
 * Edia Skin Manager Logic Impl
 * %%
 * Copyright (C) 2007 - 2013 Edia
 * %%
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *             http://opensource.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package nl.edia.sakai.tool.skinmanager.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import nl.edia.sakai.tool.skinmanager.SkinArchiveService;
import nl.edia.sakai.tool.skinmanager.model.SkinArchive;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.sakaiproject.site.api.Site;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SkinArchiveServiceImpl extends HibernateDaoSupport implements SkinArchiveService {

	@Override
	public SkinArchive createSkinArchive(final String name, final InputStream file, final Date time, final String comment) {
		return (SkinArchive) getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				int myVersion = 0;
				Number myHighestVersion = (Number) session.createCriteria(SkinArchive.class).add(
						Restrictions.eq("name", name)).setProjection(Projections.max("version")).uniqueResult();
				if (myHighestVersion != null) {
					myVersion = myHighestVersion.intValue() + 1;
				}

				SkinArchive mySkinArchive = new SkinArchive();
				mySkinArchive.setActive(true);
				mySkinArchive.setLastModified(new Timestamp(time.getTime()));
				mySkinArchive.setName(name);
				mySkinArchive.setVersion(myVersion);
				mySkinArchive.setComment(comment);
				try {
					mySkinArchive.setFile(Hibernate.createBlob(file));
				} catch (IOException e) {
					throw new HibernateException(e);
				}
				session.save(mySkinArchive);
				setSkinStatus(name, true);
				return mySkinArchive;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SkinArchive> fetchActiveSkinArchives() {
		return (List<SkinArchive>) getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria myCriteria = session
						.createCriteria(SkinArchive.class)
						.add(Restrictions.eq("active", Boolean.TRUE))
						.add(
								Restrictions
								.sqlRestriction(" {alias}.skin_version = ("
										+ " select max(sa.skin_version) from edia_skinmanager_archive sa where sa.skin_name = {alias}.skin_name "
										+ " ) "));
				return myCriteria.list();
			}
		});
	}

	@Override
	public void fetchSkinArchiveData(final String name, final int version, final OutputStream out) {
		getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SkinArchive myFindSkinArchive = findSkinArchive(name, version, session);

				InputStream myIn = myFindSkinArchive.getFile().getBinaryStream();
				try {
					writeData(out, myIn);
					return null;
				} catch (IOException e) {
					throw new HibernateException(e);
				}
			}
		});
	}

	@Override
	public void fetchSkinArchiveData(final String name, final OutputStream out) {
		getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SkinArchive myFindSkinArchive = findSkinArchive(name, session);

				InputStream myIn = myFindSkinArchive.getFile().getBinaryStream();
				try {
					writeData(out, myIn);
					return null;
				} catch (IOException e) {
					throw new HibernateException(e);
				}
			}
		});
	}

	@Override
	public SkinArchive findSkinArchive(final String name) {
		return (SkinArchive) getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return findSkinArchive(name, session);
			}
		});
	}

	@Override
	public SkinArchive findSkinArchive(final String name, final int version) {
		return (SkinArchive) getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return findSkinArchive(name, version, session);
			}
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SkinArchive> findSkinHistory(final String name) {
		return (List<SkinArchive>) getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return findSkinHistory(name, session);
			}
		});

	}

	@Override
	public void removeSkinArchive(final String name) {
		setSkinStatus(name, false);
	}

	public void setSkinStatus(final String name, final boolean enabled) {
		getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Iterator<SkinArchive> myIterator = findSkinHistory(name, session).iterator();
				while (myIterator.hasNext()) {
					SkinArchive mySkinArchive = myIterator.next();
					mySkinArchive.setActive(enabled);
				}
				// String myQueryText = "update SkinArchive SET active = :active
				// WHERE name = :name ";
				// Query myQuery = session.createQuery(myQueryText);
				// myQuery.setBoolean("active", enabled);
				// myQuery.setString("name", name);
				// myQuery.executeUpdate();
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	protected List<SkinArchive> findSkinHistory(final String name, Session session) {
		return session.createCriteria(SkinArchive.class).add(Restrictions.eq("name", name)).addOrder(
				Order.desc("version")).list();
	}

	protected void writeData(final OutputStream out, InputStream in) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
		out.close();
		in.close();
	}

	private SkinArchive findSkinArchive(final String name, int version, Session session) {
		return (SkinArchive) session.createCriteria(SkinArchive.class)
				.add(Restrictions.eq("name", name))
				.add(Restrictions.eq("version", Integer.valueOf(version))).uniqueResult();
	}

	private SkinArchive findSkinArchive(final String name, Session session) {
		return (SkinArchive) session.createCriteria(SkinArchive.class).add(Restrictions.eq("name", name)).addOrder(
				Order.desc("version")).setMaxResults(1).uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Site> findSites(final String skin, final boolean isDefault) {
		return (List<Site>) getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query;
				if (isDefault) {
					query = session.getNamedQuery("findSitesWithDefaultSkin");
				} else {
					query = session.getNamedQuery("findSitesWithSkin");

				}
				query.setString("skin", skin);
				return query.list();
			}
		});
	}

	@Override
	public Date fetchSkinArchiveDate(String name) {
		final SkinArchive archive = findSkinArchive(name);
		if (archive != null) {
			return archive.getLastModified();
		}
		return new Date();
	}

}
