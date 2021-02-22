package com.fkhwl.starter.id.converter;

import com.fkhwl.starter.id.entity.Id;
import com.fkhwl.starter.id.entity.IdMeta;
import com.fkhwl.starter.id.entity.IdMetaFactory;
import com.fkhwl.starter.id.enums.IdType;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import lombok.Data;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.06.24 16:18
 * @since 1.5.0
 */
@Data
public class IdConverterImpl implements IdConverter {

    /** Id meta */
    private IdMeta idMeta;

    /**
     * Id converter
     *
     * @since 1.5.0
     */
    @Contract(pure = true)
    public IdConverterImpl() {
    }

    /**
     * Id converter
     *
     * @param idType id type
     * @since 1.5.0
     */
    public IdConverterImpl(IdType idType) {
        this(IdMetaFactory.getIdMeta(idType));
    }

    /**
     * Id converter
     *
     * @param idMeta id meta
     * @since 1.5.0
     */
    @Contract(pure = true)
    public IdConverterImpl(IdMeta idMeta) {
        this.idMeta = idMeta;
    }

    /**
     * Convert
     *
     * @param id id
     * @return the long
     * @since 1.5.0
     */
    @Override
    public long convert(Id id) {
        return this.doConvert(id, this.idMeta);
    }

    /**
     * 通过 {@link IdMeta} 获取每个属性所在 {@link Id} 的位置, 然后通过左移将各个属性拼接为一个长整型数字 id.
     *
     * @param id     id
     * @param idMeta id meta
     * @return the long
     * @since 1.5.0
     */
    protected long doConvert(@NotNull Id id, @NotNull IdMeta idMeta) {
        long ret = 0;

        ret |= id.getMachine();

        ret |= id.getSeq() << idMeta.getSeqBitsStartPos();

        ret |= id.getTime() << idMeta.getTimeBitsStartPos();

        ret |= id.getDeployType() << idMeta.getGenMethodBitsStartPos();

        ret |= id.getIdType() << idMeta.getTypeBitsStartPos();

        ret |= id.getVersion() << idMeta.getVersionBitsStartPos();

        return ret;
    }

    /**
     * Convert
     *
     * @param id id
     * @return the id
     * @since 1.5.0
     */
    @Override
    public Id convert(long id) {
        return this.doConvert(id, this.idMeta);
    }

    /**
     * 使用无符号右移将长整型 id 反解为 {@link Id}
     *
     * @param id     id
     * @param idMeta id meta
     * @return the id
     * @since 1.5.0
     */
    protected Id doConvert(long id, @NotNull IdMeta idMeta) {
        Id ret = new Id();

        ret.setMachine(id & idMeta.getMachineBitsMask());

        ret.setSeq((id >>> idMeta.getSeqBitsStartPos()) & idMeta.getSeqBitsMask());

        ret.setTime((id >>> idMeta.getTimeBitsStartPos()) & idMeta.getTimeBitsMask());

        ret.setDeployType((id >>> idMeta.getGenMethodBitsStartPos()) & idMeta.getGenMethodBitsMask());

        ret.setIdType((id >>> idMeta.getTypeBitsStartPos()) & idMeta.getTypeBitsMask());

        ret.setVersion((id >>> idMeta.getVersionBitsStartPos()) & idMeta.getVersionBitsMask());

        return ret;
    }

}
