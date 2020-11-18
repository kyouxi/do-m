package demo.classmodify;

public class ByteUtils {

    public static int bytes2Int(byte[] b, int start ,int len){
        int sum = 0;
        int end = start + len;
        for(int i= start; i< end ;i++){
            int n= ((int) b[i]) & 0xff; //去除符号位，转成正数
            n <<= (--len) * 8; //左移 (len-1)*8 位，因为一个字节是 8 位
            sum = n + sum;
        }
        return sum;
    }

    public static byte[] int2Bytes(int value, int len){
        byte[] b = new byte[len];
        for(int i=0; i<len; i++){
            b[len-i-1] = (byte) ((value >> 8*i) & 0xff); //每次截取 8位 一个字节 存储进字节数组
        }
        return b;
    }

    public static String bytes2String(byte[] b, int start ,int len){
        return new String(b,start,len);
    }

    public static byte[] string2Bytes(String str){
        return str.getBytes();
    }

    //将originalBytes 从 offset 位置开始 len长度的 字节 替换成 replaceBytes
    public static byte[] bytesReplace(byte[] originalBytes, int offset, int len, byte[] replaceBytes){
        byte[] newBytes = new byte[originalBytes.length + (replaceBytes.length - len)];
        System.arraycopy(originalBytes, 0, newBytes, 0, offset);
        System.arraycopy(replaceBytes, 0, newBytes, offset, replaceBytes.length);
        System.arraycopy(originalBytes, offset + len, newBytes, offset + replaceBytes.length,originalBytes.length - offset - len);
        return  newBytes;
    }
}
