/**
 * @author ymj
 * @Date： 2019/12/19 13:00
 */
public class test {
    public static void main(String[] args) {
        String hexString = "0C 02 12 01 15 01 06 02 01 a4 07 52";
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i+1), 16));
        }
        for (byte v : bytes) {
            System.out.print(v + " ");
        }
    }
}
