package org.weatherspotify;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
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
        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        ipAddresses.add(netint.getName() + ": " + inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            return "Error retrieving network interfaces: " + e.getMessage();
        }

        if (ipAddresses.isEmpty()) {
            return "No non-loopback IP addresses found";
        } else {
            return "IP Addresses:\n" + String.join("\n", ipAddresses);
        }
    }
}