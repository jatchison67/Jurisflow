package com.jurisflow.role;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.TreeSet;

public final class PermissionCodes {

    private PermissionCodes() {
    }

    // -----------------------------------------------------------------
    // Tenant
    // -----------------------------------------------------------------

    public static final String TENANT_MANAGE = "tenant.manage";

    // -----------------------------------------------------------------
    // Users
    // -----------------------------------------------------------------

    public static final String USER_CREATE = "user.create";
    public static final String USER_READ = "user.read";
    public static final String USER_UPDATE = "user.update";
    public static final String USER_DELETE = "user.delete";

    // -----------------------------------------------------------------
    // Matters
    // -----------------------------------------------------------------

    public static final String MATTER_CREATE = "matter.create";
    public static final String MATTER_READ = "matter.read";
    public static final String MATTER_UPDATE = "matter.update";
    public static final String MATTER_DELETE = "matter.delete";

    // -----------------------------------------------------------------
    // Documents
    // -----------------------------------------------------------------

    public static final String DOCUMENT_UPLOAD = "document.upload";
    public static final String DOCUMENT_DOWNLOAD = "document.download";

    // -----------------------------------------------------------------
    // Calendar
    // -----------------------------------------------------------------

    public static final String CALENDAR_CREATE = "calendar.create";
    public static final String CALENDAR_READ = "calendar.read";
    public static final String CALENDAR_UPDATE = "calendar.update";
    public static final String CALENDAR_DELETE = "calendar.delete";

    // -----------------------------------------------------------------
    // Clients
    // -----------------------------------------------------------------

    public static final String CLIENT_CREATE = "client.create";
    public static final String CLIENT_READ = "client.read";
    public static final String CLIENT_UPDATE = "client.update";
    public static final String CLIENT_DELETE = "client.delete";

    // -----------------------------------------------------------------
    // Billing
    // -----------------------------------------------------------------

    public static final String BILLING_CREATE = "billing.create";
    public static final String BILLING_READ = "billing.read";
    public static final String BILLING_UPDATE = "billing.update";
    public static final String BILLING_DELETE = "billing.delete";

    // -----------------------------------------------------------------
    // Audit
    // -----------------------------------------------------------------

    public static final String AUDIT_READ = "audit.read";

    /**
     * Returns every permission defined in this class.
     */
    public static Set<String> allPermissions() {

        Set<String> permissions = new TreeSet<>();

        try {

            for (Field field : PermissionCodes.class.getDeclaredFields()) {

                if (Modifier.isStatic(field.getModifiers())
                        && Modifier.isFinal(field.getModifiers())
                        && field.getType().equals(String.class)) {

                    permissions.add((String) field.get(null));
                }
            }

        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(
                    "Unable to enumerate permission codes.", ex);
        }

        return Set.copyOf(permissions);
    }
}