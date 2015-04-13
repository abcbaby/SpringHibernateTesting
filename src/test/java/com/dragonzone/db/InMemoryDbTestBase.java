package com.dragonzone.db;

import java.sql.Connection;

import javax.sql.DataSource;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dragonzone.dao.DaoFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class InMemoryDbTestBase {
	@Autowired
	private LocalSessionFactoryBean sf;

	@Autowired
	private DataSource dataSource;

	private Configuration cfg;
	private Connection conn;

	@Autowired
	private DaoFactory daoFactoy;

	@Before
	public void setup() throws Exception {
		if (null == cfg) {
			if (null == sf) {
				throw new IllegalStateException(
						"No LocalSessionFactoryBean; perhaps you didn't setup @RunWith and @ContextConfiguration on your test?");
			}
			cfg = sf.getConfiguration();
		}

		conn = dataSource.getConnection();
		SchemaExport exporter = new SchemaExport(cfg, conn);
		exporter.execute(true, true, false, true);

		if (null != exporter.getExceptions()
				&& exporter.getExceptions().size() > 0) {
			throw new IllegalStateException(
					"Unable to setup schema; export failed");
		}
	}

	@After
	public void teardown() throws Exception {
		// see http://hsqldb.org/doc/guide/running-chapt.html#rgc_closing_db
		if (null != conn) {
			conn.createStatement().execute("SHUTDOWN");
			conn.close();
			conn = null;
		}
	}

	public DaoFactory getDaoFactoy() {
		return daoFactoy;
	}

	public void setDaoFactoy(DaoFactory daoFactoy) {
		this.daoFactoy = daoFactoy;
	}
}