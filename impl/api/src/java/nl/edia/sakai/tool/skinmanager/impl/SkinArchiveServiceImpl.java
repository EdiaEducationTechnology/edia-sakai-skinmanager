/*
 * $Author: roland $
 * $Revision: 1098 $
 * $Date: 2007-04-01 01:27:40 +0200 (zo, 01 apr 2007) $
 * 
 * Edia Project edia-sakai-skin-manager
 * Copyright (C) 2007 Roland, Edia Educatie Technologie
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package nl.edia.sakai.tool.skinmanager.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import nl.edia.sakai.tool.skinmanager.SkinArchiveService;
import nl.edia.sakai.tool.skinmanager.model.SkinArchive;

import org.apache.commons.lang.StringUtils;
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

	public SkinArchive createSkinArchive(final String name, final InputStream file, final Date time, final String comment) {
		return (SkinArchive) getHibernateTemplate().execute(new HibernateCallback() {
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
	public List<SkinArchive> fetchActiveSkinArchives() {
		return (List<SkinArchive>) getHibernateTemplate().execute(new HibernateCallback() {
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

	public void fetchSkinArchiveData(final String name, final int version, final OutputStream out) {
		getHibernateTemplate().execute(new HibernateCallback() {
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

	public void fetchSkinArchiveData(final String name, final OutputStream out) {
		getHibernateTemplate().execute(new HibernateCallback() {
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

	public SkinArchive findSkinArchive(final String name) {
		return (SkinArchive) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return findSkinArchive(name, session);
			}
		});
	}

	public SkinArchive findSkinArchive(final String name, final int version) {
		return (SkinArchive) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return findSkinArchive(name, version, session);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<SkinArchive> findSkinHistory(final String name) {
		return (List<SkinArchive>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return findSkinHistory(name, session);
			}
		});

	}

	public void removeSkinArchive(final String name) {
		setSkinStatus(name, false);
	}

	public void setSkinStatus(final String name, final boolean enabled) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Iterator<SkinArchive> myIterator = findSkinHistory(name, session).iterator();
				while (myIterator.hasNext()) {
					SkinArchive mySkinArchive = (SkinArchive) myIterator.next();
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
		return (List<SkinArchive>)session.createCriteria(SkinArchive.class).add(Restrictions.eq("name", name)).addOrder(
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
			.add(Restrictions.eq("version", new Integer(version))).uniqueResult();
	}

	private SkinArchive findSkinArchive(final String name, Session session) {
		return (SkinArchive) session.createCriteria(SkinArchive.class).add(Restrictions.eq("name", name)).addOrder(
				Order.desc("version")).setMaxResults(1).uniqueResult();
	}

	@SuppressWarnings("unchecked")
    public List<Site> findSites(final String skin) {
		if (StringUtils.isNotEmpty(skin)) {
			return (List<Site>) getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					Query query = session.getNamedQuery("findSitesWithSkin");
					query.setString("skin", skin);
					return query.list();
				}
			});
		}
		return Collections.emptyList();
	}

}
