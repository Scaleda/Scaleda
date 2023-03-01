package top.criwits.scaleda.idea.utils;

import io.grpc.ServerProvider;
import io.grpc.ServerRegistry;

import java.util.*;

import static io.grpc.InternalServiceProviders.getCandidatesViaServiceLoader;

public class RpcServicePatch {
    private static ServerRegistry instance;

    public static <T> List<T> loadAll(Class<T> klass, ClassLoader cl) {
        Iterable<T> candidates = getCandidatesViaServiceLoader(klass, cl);
        List<T> list = new LinkedList<>();
        for (T i : candidates) {
            System.out.println(i.toString());
            list.add(i);
        }
        return Collections.unmodifiableList(list);
    }

    // public static ServerRegistry getDefaultRegistry() {
    //     if (instance == null) {
    //         List<ServerProvider> providerList = loadAll(
    //                 ServerProvider.class,
    //                 Collections.emptyList(),
    //                 ServerProvider.class.getClassLoader());
    //         instance = new ServerRegistry();
    //         for (ServerProvider provider : providerList) {
    //             // logger.fine("Service loader found " + provider);
    //             try {
    //                 if (provider.getClass().getMethod("isAvailable").invoke(provider)) {
    //                     // instance.addProvider(provider);
    //                     instance.getClass().getMethod("addProvider").invoke(provider);
    //                 }
    //                 instance.getClass().getMethod("refreshProviders").invoke(instance);
    //             } catch (Throwable e) {
    //                 throw new RuntimeException(e);
    //             }
    //         }
    //     }
    //     return instance;
    // }
    public static ServerProvider getDefaultServerProvider() {
        // List<ServerProvider> providerList = loadAll(
        //         ServerProvider.class,
        //         ServerProvider.class.getClassLoader());
        // return providerList.get(0);
        return getCandidatesViaServiceLoader(ServerProvider.class, ServerProvider.class.getClassLoader()).iterator().next();
    }
}
