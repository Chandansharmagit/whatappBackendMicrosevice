package authentications.authentications.config;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtBlacklistService {

    // A Set to store blacklisted tokens
    private final Set<String> blacklistedTokens = new HashSet<>();

    // Method to add token to blacklist
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    // Method to check if a token is blacklisted
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    // Method to remove a token from the blacklist if needed (optional)
    public void removeTokenFromBlacklist(String token) {
        blacklistedTokens.remove(token);
    }

    // Method to clear all blacklisted tokens (optional)
    public void clearBlacklist() {
        blacklistedTokens.clear();
    }
}
