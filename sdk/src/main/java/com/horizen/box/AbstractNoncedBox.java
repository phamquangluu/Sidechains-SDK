package com.horizen.box;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Longs;
import com.horizen.ScorexEncoding;
import com.horizen.box.data.AbstractBoxData;
import com.horizen.proposition.Proposition;
import scorex.crypto.hash.Blake2b256;

import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractNoncedBox<P extends Proposition, BD extends AbstractBoxData<P>> extends ScorexEncoding implements NoncedBox<P>
{
    protected BD boxData;
    protected long nonce;

    public AbstractNoncedBox(BD boxData,
                             long nonce)
    {
        Objects.requireNonNull(boxData, "boxData must be defined");

        this.boxData = boxData;
        this.nonce = nonce;
    }

    @Override
    public final long value() {
        return boxData.value();
    }

    @Override
    public final P proposition() { return boxData.proposition(); }

    @Override
    public final long nonce() { return nonce; }

    @Override
    public byte[] id() {
        return Blake2b256.hash(Bytes.concat(boxData.proposition().bytes(), Longs.toByteArray(nonce)));
    }

    @Override
    public abstract byte[] bytes();

    @Override
    public int hashCode() {
        return boxData.proposition().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(this.getClass().equals(obj.getClass())))
            return false;
        if (obj == this)
            return true;
        return Arrays.equals(id(), ((AbstractNoncedBox) obj).id())
                && value() == ((AbstractNoncedBox) obj).value();
    }

    @Override
    public String toString() {
        return String.format("%s(id: %s, proposition: %s, value: %d, nonce: %d)", this.getClass().toString(), encoder().encode(id()), proposition(), value(), nonce());
    }
}


