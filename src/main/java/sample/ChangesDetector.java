package sample;

import java.sql.ResultSet;
import java.sql.SQLException;

class ChangesDetector {

    static class ChangeValues {
        double[] old;
        double[] current;
        String[] actionType;
        String[] comment;
        boolean change;

        public boolean isChange() {
            return change;
        }

        public void setChange(boolean change) {
            this.change = change;
        }

        public double[] getOld() {
            return old;
        }

        public void setOld(double[] old) {
            this.old = old;
        }

        public double[] getCurrent() {
            return current;
        }

        public void setCurrent(double[] current) {
            this.current = current;
        }

        String[] getActionType() {
            return actionType;
        }

        void setActionType(String[] actionType) {
            this.actionType = actionType;
        }

        public String[] getComment() {
            return comment;
        }

        public void setComment(String[] comment) {
            this.comment = comment;
        }
    }

    static String getTrxComment(ResultSet resultSet) throws SQLException {
        String trxComment;
        if (resultSet.getString(21) == null && resultSet.getInt(4) == 2) {
            trxComment = "New Site";
            System.out.println("new site in trx comment");
            return trxComment;
        }
        int trx = resultSet.getInt(6) - resultSet.getInt(26);

        if (trx == 0)
            trxComment = "";
        else if (trx > 0)
            trxComment = "Upgrade";
        else
            trxComment = "Downgrade";
        return trxComment;
    }

    static String getTxModeComment(ResultSet resultSet) throws SQLException {
        String txModeComment;
        if (resultSet.getString(21) == null && resultSet.getInt(4) == 2) {
            txModeComment = "New Site";
            System.out.println("new site in gtrx comment");
            return txModeComment;
        }
        if (resultSet.getInt(7) == resultSet.getInt(27))
            txModeComment = "";
        else
            txModeComment = "Upgrade";
        return txModeComment;
    }

    static String getChannelElementsComment(ResultSet resultSet) throws SQLException {
        String channelElementsComment = "";
        if (!(resultSet.getString(21) == null && resultSet.getInt(4) == 3))
//        {
//            channelElementsComment = "New Site";
//            System.out.println("new site in Ce comment"+resultSet.getString(2));
//            return channelElementsComment;
//        }
        {
            int channelElements = resultSet.getInt(9) - resultSet.getInt(29);
            if (channelElements == 0)
                channelElementsComment = "";
            else if (channelElements > 0)
                channelElementsComment = "Upgrade";
            else
                channelElementsComment = "Downgrade";
            return channelElementsComment;
        }
        return channelElementsComment;
    }

    static ChangeValues getCarriersChanges(ResultSet resultSet) throws SQLException {
        ChangeValues carriersChangeValues = new ChangeValues();
        if (resultSet.getString(21) == null && resultSet.getInt(4) == 3) {
            carriersChangeValues.setComment(new String[]{"New Site"});
            return carriersChangeValues;
        }

        int oldCarriers[] = new int[3];
        int currentCarriers[] = new int[3];

        String currentCarrIdentifier = resultSet.getString(10);
        String oldCarrIdentifier = resultSet.getString(30);
        boolean deltaCarriers = currentCarrIdentifier.equals(oldCarrIdentifier);
        if (deltaCarriers || resultSet.getInt(25) == 0)
            carriersChangeValues.setChange(false);
        else {
            carriersChangeValues.setChange(true);
            String[] currentCarriersSplit = currentCarrIdentifier.split("\\.");
            String[] oldCarriersSplit = oldCarrIdentifier.split("\\.");

            for (int i = 0; i < currentCarriersSplit.length; i++) {
                currentCarriers[i] = Integer.valueOf(currentCarriersSplit[i]);
                oldCarriers[i] = Integer.valueOf(oldCarriersSplit[i]);
            }

            String[] actionType = new String[]{"", "", ""};

            if (!currentCarriersSplit[0].equals(oldCarriersSplit[0]))
                actionType[0] = "2nd Carrier";
            if (!currentCarriersSplit[1].equals(oldCarriersSplit[1]))
                actionType[1] = ("3rd Carrier");
            if (!currentCarriersSplit[2].equals(oldCarriersSplit[2]))
                actionType[2] = ("U900");

            carriersChangeValues.setActionType(actionType);

            carriersChangeValues.setOld(new double[]{oldCarriers[0], oldCarriers[1], oldCarriers[2]});
            carriersChangeValues.setCurrent(new double[]{currentCarriers[0], currentCarriers[1], currentCarriers[2]});

            String[] carrierComments = new String[]{"", "", ""};
            if ((currentCarriers[0] > oldCarriers[0]))
                carrierComments[0] = "Upgrade";
            if (currentCarriers[1] > oldCarriers[1])
                carrierComments[1] = "Upgrade";
            if (currentCarriers[2] > oldCarriers[2])
                carrierComments[2] = "Upgrade";

            carriersChangeValues.setComment(carrierComments);

        }
        return carriersChangeValues;
    }

    static ChangeValues getTokenChanges(ResultSet resultSet) throws SQLException {
        ChangeValues tokenChangeValues = new ChangeValues();
        if (resultSet.getString(21) == null && resultSet.getInt(4) == 3) {
            tokenChangeValues.setComment(new String[]{"New Site"});
            return tokenChangeValues;
        }

        int oldTokens[] = new int[4];
        int currentTokens[] = new int[4];

        String currentTokensIdentifier = resultSet.getString(8);
        String oldTokensIdentifier = resultSet.getString(28);
        boolean deltaTokens = currentTokensIdentifier.equals(oldTokensIdentifier);
        if (deltaTokens)
            tokenChangeValues.setChange(false);
        else {
            String[] currentTokensSplit = currentTokensIdentifier.split("\\.");
            String[] oldTokensSplit = oldTokensIdentifier.split("\\.");

            for (int i = 0; i < currentTokensSplit.length; i++) {
                currentTokens[i] = Integer.valueOf(currentTokensSplit[i]);
                oldTokens[i] = Integer.valueOf(oldTokensSplit[i]);
            }
            String[] actionType = new String[]{"", "", "", ""};

            if (currentTokens[0] != oldTokens[0])
                actionType[0] = "HSUPA";
            if (currentTokens[1] != oldTokens[1])
                actionType[1] = "HSDPA3";
            if (currentTokens[2] != oldTokens[2])
                actionType[2] = "HSDPA2";
            if (currentTokens[3] != oldTokens[3])
                actionType[3] = "HSDPA1";

            tokenChangeValues.setActionType(actionType);

            tokenChangeValues.setOld(new double[]{oldTokens[0], oldTokens[1], oldTokens[2], oldTokens[3]});
            tokenChangeValues.setCurrent(new double[]{currentTokens[0], currentTokens[1], currentTokens[2], currentTokens[3]});

            String[] tokenComments = new String[]{"", "", "", ""};

            if (actionType[0].equals("HSUPA") && oldTokens[0] != 0 && currentTokens[0] != 0) {
                if ((currentTokens[0] > oldTokens[0]))
                    tokenComments[0] = "Upgrade";
                else tokenComments[0] = "Downgrade";
                tokenChangeValues.setChange(true);
            }
            String[] names = {"HSDPA3", "HSDPA2", "HSDPA1"};
            for (int i = 1; i <= names.length; i++) {
                if (actionType[i].equals(names[i - 1])) {
                    if ((currentTokens[i] == oldTokens[i]))
                        tokenComments[i] = "";
                    else {
                        tokenChangeValues.setChange(true);
                        if ((currentTokens[i] > oldTokens[i]))
                            tokenComments[i] = "Upgrade";
                        else
                            tokenComments[i] = "Downgrade";
                    }
                }
            }
            tokenChangeValues.setComment(tokenComments);
        }
        return tokenChangeValues;
    }


    static ChangeValues getPowerChanges(ResultSet resultSet) throws SQLException {
        ChangeValues powerChangeValues = new ChangeValues();
        if (resultSet.getString(21) == null && resultSet.getInt(4) == 3) {
            powerChangeValues.setComment(new String[]{"New Site"});
            return powerChangeValues;
        }

        double oldPowers[] = new double[2];
        double currentPowers[] = new double[2];

        String currentPowerIdentifier = resultSet.getString(11);
        String oldPowerIdentifier = resultSet.getString(31);
        boolean deltaPowers = currentPowerIdentifier.equals(oldPowerIdentifier);
        if (deltaPowers)
            powerChangeValues.setChange(false);
        else {
            String[] currentPowersSplit = currentPowerIdentifier.split(" ");
            String[] oldPowersSplit = oldPowerIdentifier.split(" ");

            for (int i = 0; i < currentPowersSplit.length; i++) {
                currentPowers[i] = Double.valueOf(currentPowersSplit[i]);
                oldPowers[i] = Double.valueOf(oldPowersSplit[i]);
            }

            String[] actionType = new String[]{"", ""};

            if (currentPowers[0] != oldPowers[0])
                actionType[0] = "U2100";
            if (currentPowers[1] != oldPowers[1])
                actionType[1] = "U900";

            powerChangeValues.setActionType(actionType);

            powerChangeValues.setOld(new double[]{oldPowers[0], oldPowers[1]});
            powerChangeValues.setCurrent(new double[]{currentPowers[0], currentPowers[1]});

            String[] powerComments = new String[]{"", ""};

            if (actionType[0].equals("U2100") && oldPowers[0] != 0 && currentPowers[0] != 0) {
                if ((currentPowers[0] > oldPowers[0]))
                    powerComments[0] = "Upgrade";
                else powerComments[0] = "Downgrade";
                powerChangeValues.setChange(true);
            }
            if (actionType[1].equals("U900") && oldPowers[1] != 0 && currentPowers[1] != 0) {
                if ((currentPowers[1] > oldPowers[1]))
                    powerComments[1] = "Upgrade";
                else powerComments[1] = "Downgrade";
                powerChangeValues.setChange(true);
            }
            powerChangeValues.setComment(powerComments);
        }
        return powerChangeValues;
    }
//
//        float targetPower = resultSet.getFloat(11);
//        float oldPower = resultSet.getFloat(31);
//        float power = targetPower - oldPower;
//        if (power == 0)
//            powerChangeValues.setComment("");
//        else {
//            float oldUPower = ((int) (oldPower / 10)) * 10;
//            float old2100Power = (float) (Math.round((oldPower % 10) * 100) / 10.0);
//
//
//            float targetUPower = ((int) (targetPower / 10)) * 10;
//            float target2100Power = (float) (Math.round((targetPower % 10) * 100) / 10.0);
//
//            if (targetUPower == oldUPower)
//                powerChangeValues.setActionType("U2100");
//            else if (target2100Power == old2100Power)
//                powerChangeValues.setActionType("U900");
//            else
//                powerChangeValues.setActionType("U2100-U900");
//
//            powerChangeValues.setOld(new float[]{oldUPower, old2100Power});
//            powerChangeValues.setCurrent(new float[]{targetUPower, target2100Power});
//
//            if ((target2100Power == old2100Power && oldUPower == 0.0) || (targetUPower == oldUPower && old2100Power == 0.0))
//                powerChangeValues.setComment("");
//            else {
//                if (power > 0.0)
//                    powerChangeValues.setComment("Upgrade");
//                else
//                    powerChangeValues.setComment("Downgrade");
//            }
//        }
//        return powerChangeValues;


    static String getBWComment(ResultSet resultSet) throws SQLException {
        String bWComment;
        if (resultSet.getString(21) == null && resultSet.getInt(4) == 4) {
            bWComment = "New Site";
            return bWComment;
        }
        int bw = resultSet.getInt(12) - resultSet.getInt(32);
        if (bw == 0)
            bWComment = "";
        else if (bw > 0)
            bWComment = "Upgrade";
        else
            bWComment = "Downgrade";
        return bWComment;
    }
}
