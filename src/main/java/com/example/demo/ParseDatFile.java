package com.example.demo;

import com.synthbot.jasiohost.AsioChannel;
import com.synthbot.jasiohost.AsioDriver;
import com.synthbot.jasiohost.AsioDriverListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 解析dat文件
 */
public class ParseDatFile {

    public static void main(String[] args) throws Exception {
//        String path = "e:\\temp\\yom.dat";
//        File file = new File(path);
//        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
//
//        int tag;
//        byte[] buffer = new byte[1024];
//        //将源文件写入到zip文件中
//        while((tag=bis.read(buffer))!=-1){
//            System.err.println(Arrays.toString(buffer));
//        }
//        bis.close();

        // get a list of available ASIO drivers
        List<String> driverNameList = AsioDriver.getDriverNames();

        // load the names ASIO driver
        AsioDriver asioDriver = AsioDriver.getDriver(driverNameList.get(0));

        // add an AsioDriverListener in order to receive callbacks from the driver
        asioDriver.addAsioDriverListener(new AsioDriverListener() {
            @Override
            public void sampleRateDidChange(double v) {

            }

            @Override
            public void resetRequest() {

            }

            @Override
            public void resyncRequest() {

            }

            @Override
            public void bufferSizeChanged(int i) {

            }

            @Override
            public void latenciesChanged(int i, int i1) {

            }

            @Override
            public void bufferSwitch(long systemTime, long samplePosition,
                                     Set<AsioChannel> channels) {
                // fill in audio buffers here
            }

            // implement remaining AudioDriverListener interface functions
        });

        // create a Set of AsioChannels, defining which input and output channels will be used
        Set<AsioChannel> activeChannels = new HashSet<AsioChannel>();

        // configure the ASIO driver to use the given channels
        activeChannels.add(asioDriver.getChannelOutput(0));
        activeChannels.add(asioDriver.getChannelOutput(1));

        // create the audio buffers and prepare the driver to run
        asioDriver.createBuffers(activeChannels);

        // start the driver
        asioDriver.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            ie.printStackTrace(System.err);
        }

        // tear everything down
//        AsioDriver.shutdownAndUnloadDriver();

    }
}
