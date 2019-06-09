package Auth;

import static Auth.SecondFactor.verify_code;
import org.apache.commons.codec.binary.Base32;

/**
 * Java Server side class for Google Authenticator's TOTP generator Thanks to
 * Enrico's blog for the sample code:
 *
 * @see
 * http://thegreyblog.blogspot.com/2011/12/google-authenticator-using-it-in-your.html
 *
 *
 * @see http://code.google.com/p/google-authenticator
 * @see http://tools.ietf.org/id/draft-mraihi-totp-timebased-06.txt
 */
public class GoogleAuthenticator extends SecondFactor {

    public static final long liveTime = 30 * 2 * 60 * 24;

    /**
     * set the windows size. This is an integer value representing the number of
     * 30 second windows we allow The bigger the window, the more tolerant of
     * clock skew we are.
     *
     * @param s window size - must be >=1 and <=17. Other values are ignored
     */
    /**
     * Generate a random secret key. This must be saved by the server and
     * associated with the users account to verify the code displayed by Google
     * Authenticator. The user must register this secret on their device.
     *
     * @return secret key
     */
    /**
     * Return a URL that generates and displays a QR barcode. The user scans
     * this bar code with the Google Authenticator application on their
     * smartphone to register the auth code. They can also manually enter the
     * secret if desired
     *
     * @param user user id (e.g. fflinstone)
     * @param host host or system that the code is for (e.g. myapp.com)
     * @param secret the secret that was previously generated for this user
     * @return the URL for the QR code to scan
     */
    public static String getQRBarcodeURL(String user, String host, String secret) {
        String format = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s";
        return String.format(format, user, host, secret);
    }

    /**
     * Check the code entered by the user to see if it is valid
     *
     * @param secret The users secret.
     * @param code The code displayed on the users device
     * @param t The time in msec (System.currentTimeMillis() for example)
     * @return
     */
    public boolean check_code(String secret, long code, long timeMsec) {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);

        // convert unix msec time into a 30 second "window" 
        // this is per the TOTP spec (see the RFC for details)
        long t = (timeMsec / 1000L) / liveTime;
        // Window is used to check codes generated in the near past.
        // You can use this value to tune how far you're willing to go.

        for (int i = -window_size; i <= window_size; ++i) {
            long hash;
            try {
                hash = verify_code(decodedKey, t + i);
            } catch (Exception e) {
                // Yes, this is bad form - but
                // the exceptions thrown would be rare and a static configuration problem
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
                //return false;
            }
            if (hash == code) {
                return true;
            }
        }
        // The validation code is invalid.
        return false;
    }

    public long getCurrentCode(String secret, long timeMsec) {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);

        // convert unix msec time into a 30 second "window" 
        // this is per the TOTP spec (see the RFC for details)
        long t = (timeMsec / 1000L) / liveTime;
        // Window is used to check codes generated in the near past.
        // You can use this value to tune how far you're willing to go.

        try {
            return verify_code(decodedKey, t);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
