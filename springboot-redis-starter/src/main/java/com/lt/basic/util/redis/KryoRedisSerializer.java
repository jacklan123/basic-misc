package com.lt.basic.util.redis;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.lt.basic.util.kryo.KryoPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @Description : 自定义序列化工具类
 * @author lantian
 * @Date : 2019-09-20 21:49
 */
public class KryoRedisSerializer<T> implements RedisSerializer<T> {
    private static final Logger logger = LoggerFactory.getLogger(KryoRedisSerializer.class);

    private static final KryoPool kryoPool = new KryoPool();

    private Class<T> clazz;

    public KryoRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        Kryo kryo = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Output output = new Output(bos)){
            kryo = kryoPool.borrowObject();
            kryo.writeClassAndObject(output, t);
            output.close();
            return bos.toByteArray();
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            if(kryo != null){
                kryoPool.returnObject(kryo);
            }
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        Kryo kryo = null;
        if(bytes == null || bytes.length == 0){
            return null;
        }

        try(ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            Input input = new Input(bis)){
            kryo = kryoPool.borrowObject();
            @SuppressWarnings("unchecked")
            T t = (T) kryo.readClassAndObject(input);
            input.close();
            return t;
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            if(kryo != null){
                kryoPool.returnObject(kryo);
            }
        }
    }

}