package utils;

import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import play.db.DB;

import javax.sql.DataSource;

/**
 * @author wanglu
 * @date 2018/08/25
 */
public class JdbcUtil {
    private final static Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

    private final static String DATA_SOURCE_NAME;
    static {
        DATA_SOURCE_NAME = ConfigFactory.load().getString("data.source.name");
        logger.info("load datasource from conf. name: {}", DATA_SOURCE_NAME);
    }


    public static DataSource dataSource = DB.getDataSource(DATA_SOURCE_NAME);
    public static JdbcTemplate mdmpJdbcTemplate = new JdbcTemplate(dataSource);
    public static NamedParameterJdbcTemplate mdmpNamedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

}
