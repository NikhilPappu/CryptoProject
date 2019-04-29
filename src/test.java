public class test{
    public static void main(String[] args) {
        public byte hexToByte(String hexString) {
            int firstDigit = toDigit(hexString.charAt(0));
            int secondDigit = toDigit(hexString.charAt(1));
            return (byte) ((firstDigit << 4) + secondDigit);
        }
    }
}
