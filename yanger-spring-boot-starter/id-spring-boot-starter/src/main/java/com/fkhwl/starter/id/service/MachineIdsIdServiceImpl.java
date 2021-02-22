package com.fkhwl.starter.id.service;

import com.fkhwl.starter.id.entity.Id;
import com.fkhwl.starter.id.enums.IdType;
import com.fkhwl.starter.id.populater.ResetPopulator;
import com.fkhwl.starter.id.provider.MachineIdsProvider;
import com.fkhwl.starter.id.util.TimeUtils;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.06.24 16:24
 * @since 1.5.0
 */
@Slf4j
public class MachineIdsIdServiceImpl extends IdServiceImpl {

    /** Last timestamp */
    protected long lastTimestamp = -1;

    /** Machine id map */
    protected Map<Long, Long> machineIdMap = new ConcurrentHashMap<Long, Long>();
    /** STORE_FILE_NAME */
    public static final String STORE_FILE_NAME = "machineIdInfo.store";
    /** Store file path */
    private String storeFilePath;
    /** Store file */
    private File storeFile;
    /** Lock */
    private final Lock lock = new ReentrantLock();

    /**
     * Init
     *
     * @since 1.5.0
     */
    @Override
    public void init() {
        if (!(this.machineIdProvider instanceof MachineIdsProvider)) {
            log.error("The machineIdProvider is not a MachineIdsProvider instance so that Vesta Service refuses to start.");
            throw new RuntimeException(
                "The machineIdProvider is not a MachineIdsProvider instance so that Vesta Service refuses to start.");
        }
        super.init();
        this.initStoreFile();
        this.initMachineId();
    }

    /**
     * Populate id
     *
     * @param id id
     * @since 1.5.0
     */
    @Override
    protected void populateId(Id id) {
        this.supportChangeMachineId(id);
    }

    /**
     * Support change machine id
     *
     * @param id id
     * @since 1.5.0
     */
    private void supportChangeMachineId(@NotNull Id id) {
        try {
            id.setMachine(this.machineId);
            this.idPopulator.populateId(id, this.idMeta);
            this.lastTimestamp = id.getTime();
        } catch (IllegalStateException e) {
            log.warn("Clock moved backwards, change MachineId and reset IdPopulator");
            this.lock.lock();
            try {
                if (id.getMachine() == this.machineId) {
                    this.changeMachineId();
                    this.resetIdPopulator();
                }
            } finally {
                this.lock.unlock();
            }
            this.supportChangeMachineId(id);
        }
    }

    /**
     * Change machine id
     *
     * @since 1.5.0
     */
    protected void changeMachineId() {
        this.machineIdMap.put(this.machineId, this.lastTimestamp);
        this.storeInFile();
        this.initMachineId();
    }

    /**
     * Reset id populator
     *
     * @since 1.5.0
     */
    protected void resetIdPopulator() {
        if (this.idPopulator instanceof ResetPopulator) {
            ((ResetPopulator) this.idPopulator).reset();
        } else {
            try {
                this.idPopulator = this.idPopulator.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e1) {
                throw new RuntimeException("Reset IdPopulator <[" + this.idPopulator.getClass().getCanonicalName() + "]> instance error",
                                           e1);
            }
        }
    }

    /**
     * Init store file
     *
     * @since 1.5.0
     */
    protected void initStoreFile() {
        if (this.storeFilePath == null || this.storeFilePath.length() == 0) {
            this.storeFilePath = System.getProperty("user.dir") + File.separator + STORE_FILE_NAME;
        }
        try {
            log.info("machineId info store in <[" + this.storeFilePath + "]>");
            this.storeFile = new File(this.storeFilePath);
            if (this.storeFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(this.storeFile));
                String line = reader.readLine();
                while (line != null && line.length() > 0) {
                    String[] kvs = line.split(":");
                    if (kvs.length == 2) {
                        this.machineIdMap.put(Long.parseLong(kvs[0]), Long.parseLong(kvs[1]));
                    } else {
                        throw new IllegalArgumentException(this.storeFile.getAbsolutePath() + " has illegal value <[" + line + "]>");
                    }
                    line = reader.readLine();
                }
                reader.close();
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * Init machine id
     *
     * @since 1.5.0
     */
    protected void initMachineId() {
        long startId = this.machineId;
        long newMachineId = this.machineId;
        while (true) {
            if (this.machineIdMap.containsKey(newMachineId)) {
                long timestamp = TimeUtils.genTime(IdType.parse(this.idTypeValue));
                if (this.machineIdMap.get(newMachineId) < timestamp) {
                    this.machineId = newMachineId;
                    break;
                } else {
                    newMachineId = ((MachineIdsProvider) this.machineIdProvider).getNextMachineId();
                }
                if (newMachineId == startId) {
                    throw new RuntimeException("No machineId is available");
                }
            } else {
                this.machineId = newMachineId;
                break;
            }
        }
    }

    /**
     * Store in file
     *
     * @since 1.5.0
     */
    protected void storeInFile() {
        try (Writer writer = new FileWriter(this.storeFile, false)) {
            for (Map.Entry<Long, Long> entry : this.machineIdMap.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            log.error("Write machineId info to File<[" + this.storeFile.getAbsolutePath() + "]> error");
            throw new RuntimeException("Write machineId info to File<[" + this.storeFile.getAbsolutePath() + "]> error");
        }
    }

    /**
     * Sets store file path *
     *
     * @param storeFilePath store file path
     * @since 1.5.0
     */
    public void setStoreFilePath(String storeFilePath) {
        this.storeFilePath = storeFilePath;
    }
}
