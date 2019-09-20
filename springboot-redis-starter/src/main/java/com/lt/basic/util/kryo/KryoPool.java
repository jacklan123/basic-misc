package com.lt.basic.util.kryo;

import com.esotericsoftware.kryo.Kryo;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * @author lantian
 * @date 2019/09/20
 */
public class KryoPool extends GenericObjectPool<Kryo> {

    public KryoPool() {
        super(new KryoFactory());
        this.setMaxIdle(500);
        this.setMinIdle(50);
        this.setMaxTotal(60000);
        this.setMaxWaitMillis(100);
    }

    public static class KryoFactory extends BasePooledObjectFactory<Kryo> {
        @Override
        public PooledObject<Kryo> wrap(Kryo kryo) {
            return new DefaultPooledObject<Kryo>(kryo);
        }

        @Override
        public Kryo create() throws Exception {
            Kryo kryo = new Kryo();
            kryo.setRegistrationRequired(false);
            return kryo;
        }

    }

}