package cn.xa87.common.utils;


import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.io.Serializable;

/**
 * 密码加密
 * <p> TODO</p>
 *
 * @author: Bat Admin
 * @Date :          2015年9月25日 上午10:36:45
 */
public class PasswordHelper {

    private static String algorithmName = "MD5";
    private static int hashIterations = 2;
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    /**
     * 加密任何字符串
     * <p> TODO</p>
     *
     * @author: Bat Admin
     * @param: @param pwd   字符串
     * @param: @param salt  盐值
     * @param: @return
     * @return: String
     * @Date :          2016年3月30日 下午4:54:02
     * @throws:
     */
    public static String encryString(String pwd, String salt) {
        String newPassword = new SimpleHash(
                algorithmName,
                pwd,
                ByteSource.Util.bytes(salt),
                hashIterations).toHex();
        return newPassword;
    }

    public static void main(String[] args) {

        String s = encryString("a2f00354b33f6ca71a1be2246f03c05b", "e7fa8439bacf40a99d52c57593fe09c8");
        System.out.println(s);
    }

    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    /**
     * AppUser密码加密
     * 后台管理账号加密
     * <p> TODO</p>
     *
     * @author: Bat Admin
     * @param: @param appUser
     * @return: void
     * @Date :          2016年1月12日 上午11:05:16
     * @throws:
     */
    public PasswordInfo encryptPassword(String password) {

        PasswordInfo passwordInfo = new PasswordInfo();
        passwordInfo.setSalt(randomNumberGenerator.nextBytes().toHex());
        String newPassword = new SimpleHash(
                algorithmName,
                password,
                ByteSource.Util.bytes(passwordInfo.getSalt()), hashIterations).toHex();


        passwordInfo.setPassword(newPassword);
        return passwordInfo;
    }

    /**
     * 密码信息
     */
    public static class PasswordInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        String password = "";

        String salt = "";

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }
    }

}
