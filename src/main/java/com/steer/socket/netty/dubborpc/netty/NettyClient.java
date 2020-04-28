package com.steer.socket.netty.dubborpc.netty;

public class NettyClient {
//    static private Logger log = LoggerFactory.getLogger(NettyClient.class);
//
//    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//
//    private static NettyClientHandler clientHandler;
//
//    public Object getBean(final Class<?> serviceClass){
//        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class<?>[]{serviceClass},(proxy, method, args) -> {
////            log.info(" method:{} args:{}",method,args);
//            if (clientHandler == null){
//                //远程注册去取
//                initClient("127.0.0.1",8888);
//            }
//            Invocation invocation = new Invocation(serviceClass.getName(),method.getName(),method.getParameterTypes() ,args);
//            clientHandler.setInvocation(invocation);
//            //阻塞等待结果返回
//            return executorService.submit(clientHandler).get();
//        });
//    }

//    public static NettyClientHandler initClient(String host, int port){
//         NettyClientHandler clientHandler = null;
//        try {
//            clientHandler = new NettyClientHandler();
//            NioEventLoopGroup group = new NioEventLoopGroup();
//            Bootstrap bootstrap = new Bootstrap();
//            bootstrap.group(group)
//                    .channel(NioSocketChannel.class)
//                    .handler(new LoggingHandler(LogLevel.INFO))
//                    .handler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ChannelPipeline pipeline = ch.pipeline();
//                            //添加POJO对象解码器 禁止缓存类加载器
//                            ch.pipeline().addLast(new ObjectDecoder(1024, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
//                            //设置发送消息编码器
//                            ch.pipeline().addLast(new ObjectEncoder());
//
//                            pipeline.addLast(clientHandler);
//                        }
//                    });
//            ChannelFuture future = bootstrap.connect(host, port).sync();
//            future.addListener((ChannelFuture f)->{
//                if (f.isSuccess()){
//                    log.info("连接{}:{}成功",host,port);
//                }else {
//                    log.info("连接{}:{}失败",host,port);
//                }
//            });
//            return clientHandler;
////            future.channel().closeFuture().sync();
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
////            group.shutdownGracefully();
//        }
//        return null;
//    }
}
