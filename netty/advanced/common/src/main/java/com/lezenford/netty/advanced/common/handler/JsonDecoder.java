package com.lezenford.netty.advanced.common.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lezenford.netty.advanced.common.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class JsonDecoder extends MessageToMessageDecoder<ByteBuf> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        final byte[] bytes = ByteBufUtil.getBytes(msg);
        Message message = OBJECT_MAPPER.readValue(bytes, Message.class);
//        final byte[] String= ByteBufUtil.getBytes(msg);
//        Message message = OBJECT_MAPPER.readValue(String, Message.class);
        out.add(message);
    }
}
