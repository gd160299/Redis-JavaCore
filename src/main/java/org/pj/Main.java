package org.pj;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pj.model.Session;
import org.pj.model.User;
import org.pj.redis.RedisClient;
import org.pj.repository.IUserRepository;
import org.pj.repository.UserRepositoryImpl;
import org.pj.service.ISessionManager;
import org.pj.service.SessionManagerImpl;
import org.pj.util.EncryptionUtil;

import java.util.Scanner;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

            RedisClient redisClient = new RedisClient("localhost", 6379);
            ISessionManager ISessionManager = new SessionManagerImpl(redisClient);
            IUserRepository IUserRepository = new UserRepositoryImpl();
            Scanner scanner = new Scanner(System.in);

            System.out.print("Nhập tên đăng nhập: ");
            String username = scanner.nextLine();

            System.out.print("Nhập mật khẩu: ");
            String password = scanner.nextLine();

            // Xác thực người dùng
        try {
            User user = IUserRepository.findByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                // Tạo session ID (mã hóa)
                String sessionId = EncryptionUtil.encrypt(user.getUserId() + System.currentTimeMillis());

                // Tạo phiên làm việc
                Session session = new Session(sessionId, user);
                ISessionManager.createSession(sessionId, session);

                System.out.println("Đăng nhập thành công. Session ID: " + sessionId);

                // Giả lập hành động của người dùng
                while (true) {
                    System.out.println("\n1. Xem thông tin phiên");
                    System.out.println("2. Đăng xuất");
                    System.out.print("Lựa chọn của bạn: ");
                    int choice = Integer.parseInt(scanner.nextLine());

                    if (choice == 1) {
                        Session currentSession = ISessionManager.getSession(sessionId);
                        if (currentSession != null) {
                            System.out.println("Phiên làm việc của bạn:");
                            System.out.println("Session ID: " + currentSession.getSessionId());
                            System.out.println("User: " + currentSession.getUser().getUsername());
                            System.out.println("Thời gian đăng nhập: " + currentSession.getCreateTime());
                            System.out.println("Thời gian truy cập cuối: " + currentSession.getLastAccessTime());
                        } else {
                            System.out.println("Phiên làm việc đã hết hạn.");
                            break;
                        }
                    } else if (choice == 2) {
                        // Đăng xuất
                        ISessionManager.deleteSession(sessionId);
                        System.out.println("Đăng xuất thành công.");
                        break;
                    } else {
                        System.out.println("Lựa chọn không hợp lệ.");
                    }
                }
            } else {
                System.out.println("Tên đăng nhập hoặc mật khẩu không đúng.");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception: ", e);
        }
        finally {
            redisClient.close();
            scanner.close();
        }
    }
}
