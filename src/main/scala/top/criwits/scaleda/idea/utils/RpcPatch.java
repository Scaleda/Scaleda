package top.criwits.scaleda.idea.utils;

import io.grpc.ManagedChannelProvider;
import io.grpc.ServerProvider;

import static io.grpc.InternalServiceProviders.getCandidatesViaServiceLoader;

public class RpcPatch {
    public static ServerProvider getDefaultServerProvider() {
        return getCandidatesViaServiceLoader(ServerProvider.class, ServerProvider.class.getClassLoader()).iterator().next();
    }

    public static ManagedChannelProvider getDefaultClientProvider() {
        return getCandidatesViaServiceLoader(ManagedChannelProvider.class, ManagedChannelProvider.class.getClassLoader()).iterator().next();
    }
}
