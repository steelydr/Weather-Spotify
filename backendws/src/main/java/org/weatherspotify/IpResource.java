package org.weatherspotify;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/get-ip")
public class IpResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIpAddresses() {
        List<String> ipAddresses = new ArrayList<>();
        String defaultInterface = null;
        try {
            // Find the default interface
            NetworkInterface defaultNetworkInterface = NetworkInterface.getByInetAddress(InetAddress.getByName("0.0.0.0"));
            if (defaultNetworkInterface != null) {
                defaultInterface = defaultNetworkInterface.getName() + " (Default Interface)";
            }

            // List all interfaces and their IP addresses
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
                if (isPhysicalInterface(netint)) {
                    Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                    for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                        if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                            String entry = netint.getName() + ": " + inetAddress.getHostAddress();
                            ipAddresses.add(entry);
                        }
                    }
                }
            }
        } catch (Exception e) {
            return "Error retrieving network interfaces: " + e.getMessage();
        }
        
        if (ipAddresses.isEmpty()) {
            return "No non-loopback IP addresses found";
        } else {
            StringBuilder result = new StringBuilder("Network Interfaces:\n");
            if (defaultInterface != null) {
                result.append(defaultInterface).append("\n");
            }
            result.append(String.join("\n", ipAddresses));
            return result.toString();
        }
    }

    private boolean isPhysicalInterface(NetworkInterface netint) throws Exception {
        return netint.isUp() 
            && !netint.isLoopback() 
            && !netint.isVirtual() 
            && !netint.isPointToPoint() 
            && !netint.getName().contains("docker") 
            && !netint.getName().contains("vmnet") 
            && !netint.getName().contains("veth")
            && !netint.getName().contains("tun")
            && !netint.getName().contains("tap")
            && netint.getHardwareAddress() != null;
    }
}