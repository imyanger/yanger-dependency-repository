package com.yanger.starter.id.converter;

import com.yanger.starter.id.entity.Id;
import com.yanger.starter.id.entity.IdMeta;
import com.yanger.starter.id.enums.IdType;
import com.yanger.starter.id.factory.IdMetaFactory;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * ID 对象和长整型 id 互转
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Data
public class IdConverterImpl implements IdConverter {

    private IdMeta idMeta;

    @Contract(pure = true)
    public IdConverterImpl() { }

    public IdConverterImpl(IdType idType) {
        this(IdMetaFactory.getIdMeta(idType));
    }

    @Contract(pure = true)
    public IdConverterImpl(IdMeta idMeta) {
        this.idMeta = idMeta;
    }

    /**
     * ID 对象 转 长整型
     */
    @Override
    public long convert(Id id) {
        return this.doConvert(id, this.idMeta);
    }

    /**
     * 通过 {@link IdMeta} 获取每个属性所在 {@link Id} 的位置, 然后通过左移将各个属性拼接为一个长整型数字 id.
     * @param id     id
     * @param idMeta id meta
     * @return the long
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
     * 长整型 id 转 ID 对象
     */
    @Override
    public Id convert(long id) {
        return this.doConvert(id, this.idMeta);
    }

    /**
     * 使用无符号右移将长整型 id 反解为 {@link Id}
     * @param id     id
     * @param idMeta id meta
     * @return the id
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
