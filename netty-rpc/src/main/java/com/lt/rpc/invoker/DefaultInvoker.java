package com.lt.rpc.invoker;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class DefaultInvoker implements Invoker{

    private ChannelHandlerContext ctx;
    private String requestId;
    private String identify;
    private Class returnType;

    private Object result;

    public DefaultInvoker(Class returnType, ChannelHandlerContext ctx, String requestId, String identify) {
        this.returnType = returnType;
        this.ctx = ctx;
        this.requestId = requestId;
        this.identify = identify;
    }

    @SuppressWarnings("unckecked")
    @Override
    public Object invoke(Object[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("interfaces", identify);
        JSONObject param = new JSONObject();
        if (args != null) {
            for (Object obj : args) {
                param.put(obj.getClass().getName(), obj);
            }
        }
        jsonObject.put("parameter", param);
        jsonObject.put("requestId", requestId);
        System.out.println("发送给服务端JSON为：" + jsonObject.toJSONString());
        String msg = jsonObject.toJSONString() + "$$";
        ByteBuf byteBuf = Unpooled.buffer(msg.getBytes().length);
        byteBuf.writeBytes(msg.getBytes());
        ctx.writeAndFlush(byteBuf);
        waitForResult();
        return result;
    }

    @Override
    public void setResult(String result) {
        synchronized (this) {
            this.result = JSONObject.parseObject(result, returnType);
            notifyAll();
        }
    }


    private void waitForResult() {
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}