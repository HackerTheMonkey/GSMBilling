package com.asiacell.billing.postactivation;
public class SmpMessageGenerator
{
    public static void main(String args[])
    {
        // Composing the SMP message
        String messageStartFlag = "`SC`";
        String messageLength = "005C";
        // Compile the messageHeader
        String versionNumber = "1.00";
        String terminalIdentifier = "internal";
        String serviceName = "PPS" + " ";
        String messageHeader = versionNumber + terminalIdentifier + serviceName;
        System.out.println(messageHeader);
        // Compile the sessionHeader
        String sessionID = "00001322";
        String sessionControlCharacter = "DLGCON";
        String reservedField = "00000087";
        String sessionHeader = sessionID + sessionControlCharacter + reservedField;
        System.out.println(sessionHeader);
        // Compile the transactionHeader
        String transactionID = "29C3";
        String transactionControlCharacter = "TXBEG ";
        String tranReservedField = "0000";
        String transactionHeader = transactionID + transactionControlCharacter + tranReservedField;
        System.out.println(transactionHeader);
        // rest of the fields
        String mmlCommand = "DISP PPS ACNTINFO:MSISDN=7701105701" + " ";
        String msgCheckSum = "BBAFCCC5";
        // Compile the final SMP Message
        String smpMessage = messageStartFlag + messageLength + messageHeader + sessionHeader + transactionHeader + mmlCommand + msgCheckSum + ";";
        String messageHeaderBin = NumsConverter.convertStrToBin(messageHeader);
        String sessionHeaderBin = NumsConverter.convertStrToBin(sessionHeader);
        String transactionHeaderBin = NumsConverter.convertStrToBin(transactionHeader);
        String mmlCommandBin = NumsConverter.convertStrToBin(mmlCommand);
        System.out.println(mmlCommand);        
    }
}
