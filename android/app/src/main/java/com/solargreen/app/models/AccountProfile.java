package com.solargreen.app.models;

public class AccountProfile {

    public String fullName;
    public String email;
    public String phone;
    public String role;
    public String memberSince;
    public String planName;
    public String planPrice;
    public String planRenewalDate;
    public boolean autoRenew;

    public AccountProfile(String fullName, String email, String phone, String role, String memberSince,
                          String planName, String planPrice, String planRenewalDate, boolean autoRenew) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.memberSince = memberSince;
        this.planName = planName;
        this.planPrice = planPrice;
        this.planRenewalDate = planRenewalDate;
        this.autoRenew = autoRenew;
    }

    public String getInitials() {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "?";
        }
        String[] parts = fullName.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) continue;
            sb.append(Character.toUpperCase(part.charAt(0)));
            if (sb.length() == 2) break;
        }
        return sb.toString();
    }
}
