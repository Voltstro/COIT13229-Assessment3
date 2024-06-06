package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;

public class PublicKeyRequest extends Request {

    public PublicKeyRequest() {
    }

    public PublicKeyRequest(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    private byte[] publicKey;

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public void doRequest(DatabaseUtility databaseUtility) {
    }
}
