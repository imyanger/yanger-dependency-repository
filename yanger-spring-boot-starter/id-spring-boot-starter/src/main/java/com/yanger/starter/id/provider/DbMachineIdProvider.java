package com.yanger.starter.id.provider;

import cn.hutool.core.text.StrFormatter;
import com.yanger.tools.web.tools.NetUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

/**
 * 依赖数据库分配机器 id
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Slf4j
public class DbMachineIdProvider implements MachineIdProvider {

    /** Machine id */
    private long machineId;

    /** Jdbc template */
    private JdbcTemplate jdbcTemplate;

    /** SELECT_SQL */
    private static final String SELECT_SQL = "SELECT id FROM `db_machine_id_provider` WHERE ip = ?";

    /** INSERT_SQL */
    private static final String INSERT_SQL = "INSERT INTO `db_machine_id_provider`(`ip`, `id`) " +
                                             "SELECT ?, IF(MAX(id) >= 0, MAX(id) + 1, 0) FROM `db_machine_id_provider`";

    public DbMachineIdProvider() {
        log.debug("IpConfigurableMachineIdProvider constructed.");
    }

    /**
     * Init
     */
    public void init() {
        String ip = NetUtils.getLocalHost();

        if (!StringUtils.hasText(ip)) {
            String msg = "Fail to get host IP address. Stop to initialize the DbMachineIdProvider provider.";

            log.error(msg);
            throw new IllegalStateException(msg);
        }

        Long id = null;
        try {
            id = this.jdbcTemplate.queryForObject(SELECT_SQL, new Object[] {ip}, Long.class);
        } catch (EmptyResultDataAccessException e) {
            // Ignore the exception
            log.error("No allocation before for ip {}.", ip);
        }

        if (id != null) {
            this.machineId = id;
            return;
        }

        log.info("Fail to get ID from DB for host IP address {}. Next step try to allocate one.", ip);

        // 没有数据则初始化数据
        int count = this.jdbcTemplate.update(INSERT_SQL, ip);

        if (count != 1) {
            String msg = StrFormatter.format("can not insert init data for ip {}", ip);
            log.error(msg);
            throw new IllegalStateException(msg);
        }

        try {
            id = this.jdbcTemplate.queryForObject(
                SELECT_SQL,
                new Object[] {ip}, Long.class);

        } catch (EmptyResultDataAccessException e) {
            // Ignore the exception
            log.error("Fail to do allocation for ip {}.", ip);
        }

        if (id == null) {
            String msg = StrFormatter.format("Fail to get ID from DB for host IP address {} after allocation. "
                                             + "Stop to initialize the DbMachineIdProvider provider.", ip);
            log.error(msg);
            throw new IllegalStateException(msg);
        }

        this.machineId = id;
    }

    /**
     * Gets machine id
     * @return the machine id
     */
    @Override
    public long getMachineId() {
        return this.machineId;
    }

    /**
     * Sets machine id
     * @param machineId machine id
     */
    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    /**
     * Gets jdbc template
     * @return the jdbc template
     */
    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    /**
     * Sets jdbc template
     * @param jdbcTemplate jdbc template
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
