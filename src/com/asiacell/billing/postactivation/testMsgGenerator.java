package com.asiacell.billing.postactivation;
public class testMsgGenerator {

    static String MessageHeader = "1.00internalSRVM    ";//20 bytes
    static String SessionHeader = "00001322DLGLGN0000";//18 bytes
    static String TransactionHeader = "00000001TXBEG 0000"; //18 bytes
    static String OpInfo = "LOGIN:USER=billing,PSWD=billing ";
    static String targetChecksum = "9afb81c7";    
    public static void main(String[] args) {
        String MessageForXOR;
        String Hash;
        MessageForXOR = MessageHeader + SessionHeader + TransactionHeader + OpInfo;       
        System.out.println(MessageForXOR);
        if (MessageForXOR.length() % 4 != 0) {
            System.out.println("Incorrect length"); //Message is not a multiple of 4 bytes
        } else {
            Hash = CalcHash(MessageForXOR, 4);
            System.out.println(Hash);
        }

    }

    private static String CalcHash(String StringToHash, int bByte) {
        int Dummy;
        int Result = 0;
        int temp = 0;
        if (bByte > 4) {
            return "overflow";
        }

        for (int i = 0; i < StringToHash.length() - 1; i = i + 4) {//step along the string in chunks of Bytes
            Dummy = 0;
            for (int j = bByte - 1; j >= 0; j--) {// Build a "Bytes" long representation of the chunk
                // extract the byte we're interested in

                temp=(int) StringToHash.substring(i + (bByte - j - 1),(i + (bByte - j - 1))+1).charAt(0);
                //' shift bits to left

                temp = temp << 8 * j;
                //' add to running total for this block
                Dummy = Dummy + temp;
            }
            if (i == 0) {// first block, noting to xor with
                Result = Dummy;
            } else {// subsequent blocks, xor with running hash for the message
                Result = Result ^ Dummy;
            }


        }
        Result = -Result - 1;// 'take the negative

        return Integer.toHexString(Result);


    }
}