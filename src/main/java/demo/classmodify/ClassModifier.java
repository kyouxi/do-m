package demo.classmodify;

//修改字节码
public class ClassModifier {
    private static final int CONSTANT_POOL_COUNT_INDEX = 8; // 开头8个字节是魔数和版本号
    private static final int CONSTANT_Utf8_info = 1;
    //常量池中11中常量所占的长度，CONSTANT_Utf8_info（tag=1）除外，其不是定长的。 从tag=3开始，所以数组0，1，2 的值为-1
    private static final int[] CONSTANT_ITEM_LENGTH = {-1,-1,-1,5,5,9,9,3,3,5,5,5,5};
    private static final int u1 = 1;
    private static final int u2 = 2;
    private byte[] classByte;

    public ClassModifier(byte[] classByte){
        this.classByte = classByte;
    }

    //修改 CONSTANT_Utf8_info 常量的值
    public byte[] modifyUTF8Constant(String oldStr,String newStr){
        int cpc = getConstantPoolCount();  //获得常量池中常量的数量
        int offset = CONSTANT_POOL_COUNT_INDEX + u2; //第一个常量的位置, u2 两个字节用来存储常量的数量
        for(int i=0; i<cpc; i++){
            int tag = ByteUtils.bytes2Int(classByte,offset,u1); //取出offset位置的常量的 tag
            if(tag == CONSTANT_Utf8_info){  //如果tag 是 CONSTANT_Utf8_info
                int len = ByteUtils.bytes2Int(classByte, offset + u1, u2); //取出两个字节，表示该字符串长度
                offset += (u1 + u2); //offset 调整到表示字符串具体值的位置
                String str = ByteUtils.bytes2String(classByte,offset,len); //取出 len长度的 字节，并转成字符串
                if(str.equalsIgnoreCase(oldStr)){ //如果 该字符串和 oldStr 值一致
                    byte[] strBytes = ByteUtils.string2Bytes(newStr); //将 newStr转为字节
                    byte[] strLen = ByteUtils.int2Bytes(newStr.length(), u2); //将 newStr 的长度 转为字节
                    classByte = ByteUtils.bytesReplace(classByte, offset - u2, u2, strLen);
                    classByte = ByteUtils.bytesReplace(classByte,offset, len, strBytes);
                    return classByte;
                }else {
                    // 若与 oldStr 不一致，则 offset 偏移 oldStr 的长度
                    offset += len;
                }
            }else {
                //若不是 CONSTANT_Utf8_info， offset 偏移 该常量的字节大小
                offset += CONSTANT_ITEM_LENGTH[tag];
            }
        }
        return classByte;
    }

    public int getConstantPoolCount(){
        return ByteUtils.bytes2Int(classByte,CONSTANT_POOL_COUNT_INDEX, u2);
    }
}
