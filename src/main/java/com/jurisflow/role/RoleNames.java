package com.jurisflow.role;

/**
 * System-defined role names.
 *
 * These roles are automatically created for every new tenant.
 * Additional custom roles may be created by tenant administrators.
 */
public final class RoleNames {

    private RoleNames() {
        // Prevent instantiation
    }

    // Administration

    public static final String ADMINISTRATOR = "Administrator";

    public static final String MANAGING_ATTORNEY = "Managing Attorney";

    // Attorneys

    public static final String ATTORNEY = "Attorney";

    public static final String OF_COUNSEL = "Of Counsel";

    // Legal Staff

    public static final String PARALEGAL = "Paralegal";

    public static final String LEGAL_ASSISTANT = "Legal Assistant";

    public static final String LAW_CLERK = "Law Clerk";

    // Administrative Staff

    public static final String OFFICE_MANAGER = "Office Manager";

    public static final String RECEPTIONIST = "Receptionist";

    public static final String ACCOUNTING = "Accounting";

    public static final String BILLING_SPECIALIST = "Billing Specialist";

    // Clients

    public static final String CLIENT = "Client";

}