package top.criwits.scaleda.idea.utils;

import io.grpc.ServerProvider;

import static io.grpc.InternalServiceProviders.getCandidatesViaServiceLoader;

public class RpcServicePatch {
    public static ServerProvider getDefaultServerProvider() {
        return getCandidatesViaServiceLoader(ServerProvider.class, ServerProvider.class.getClassLoader()).iterator().next();
    }
}
