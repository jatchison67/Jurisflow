package com.jurisflow.role;

import java.util.Map;
import java.util.Set;

/**
 * Defines the default permissions assigned to each
 * system role when a new tenant is created.
 * <p>
 * This class is the authoritative RBAC configuration
 * for JurisFlow.
 */
public final class DefaultRoles {

    private DefaultRoles() {
    }

    public static final Map<String, Set<String>> ROLE_PERMISSIONS =
            Map.ofEntries(

                    Map.entry(
                            RoleNames.ADMINISTRATOR,
                            PermissionCodes.allPermissions()
                    ),
                    Map.entry(
                            RoleNames.MANAGING_ATTORNEY,
                            Set.of(
                                    PermissionCodes.USER_CREATE,
                                    PermissionCodes.USER_READ,
                                    PermissionCodes.USER_UPDATE,

                                    PermissionCodes.MATTER_CREATE,
                                    PermissionCodes.MATTER_READ,
                                    PermissionCodes.MATTER_UPDATE,
                                    PermissionCodes.MATTER_DELETE,

                                    PermissionCodes.DOCUMENT_UPLOAD,
                                    PermissionCodes.DOCUMENT_DOWNLOAD,

                                    PermissionCodes.CALENDAR_CREATE,
                                    PermissionCodes.CALENDAR_READ,
                                    PermissionCodes.CALENDAR_UPDATE,
                                    PermissionCodes.CALENDAR_DELETE,

                                    PermissionCodes.CLIENT_CREATE,
                                    PermissionCodes.CLIENT_READ,
                                    PermissionCodes.CLIENT_UPDATE,

                                    PermissionCodes.AUDIT_READ
                            )
                    ),
                    Map.entry(

                            RoleNames.ATTORNEY,
                            Set.of(
                                    PermissionCodes.MATTER_CREATE,
                                    PermissionCodes.MATTER_READ,
                                    PermissionCodes.MATTER_UPDATE,

                                    PermissionCodes.DOCUMENT_UPLOAD,
                                    PermissionCodes.DOCUMENT_DOWNLOAD,

                                    PermissionCodes.CALENDAR_READ,
                                    PermissionCodes.CALENDAR_UPDATE,

                                    PermissionCodes.CLIENT_READ,
                                    PermissionCodes.CLIENT_UPDATE
                            )
                    ),
                    Map.entry(

                            RoleNames.OF_COUNSEL,
                            Set.of(
                                    PermissionCodes.MATTER_READ,

                                    PermissionCodes.DOCUMENT_DOWNLOAD,

                                    PermissionCodes.CALENDAR_READ,

                                    PermissionCodes.CLIENT_READ
                            )
                    ),
                    Map.entry(

                            RoleNames.PARALEGAL,
                            Set.of(
                                    PermissionCodes.MATTER_READ,
                                    PermissionCodes.MATTER_UPDATE,

                                    PermissionCodes.DOCUMENT_UPLOAD,
                                    PermissionCodes.DOCUMENT_DOWNLOAD,

                                    PermissionCodes.CALENDAR_READ,

                                    PermissionCodes.CLIENT_READ
                            )
                    ),
                    Map.entry(

                            RoleNames.LEGAL_ASSISTANT,
                            Set.of(
                                    PermissionCodes.DOCUMENT_UPLOAD,
                                    PermissionCodes.DOCUMENT_DOWNLOAD,

                                    PermissionCodes.CALENDAR_READ,
                                    PermissionCodes.CALENDAR_UPDATE,

                                    PermissionCodes.CLIENT_READ,
                                    PermissionCodes.CLIENT_UPDATE
                            )
                    ),
                    Map.entry(

                            RoleNames.LAW_CLERK,
                            Set.of(
                                    PermissionCodes.MATTER_READ,

                                    PermissionCodes.DOCUMENT_DOWNLOAD
                            )
                    ),
                    Map.entry(

                            RoleNames.OFFICE_MANAGER,
                            Set.of(
                                    PermissionCodes.USER_READ,

                                    PermissionCodes.CALENDAR_CREATE,
                                    PermissionCodes.CALENDAR_READ,
                                    PermissionCodes.CALENDAR_UPDATE,
                                    PermissionCodes.CALENDAR_DELETE,

                                    PermissionCodes.CLIENT_CREATE,
                                    PermissionCodes.CLIENT_READ,
                                    PermissionCodes.CLIENT_UPDATE,

                                    PermissionCodes.AUDIT_READ
                            )
                    ),
                    Map.entry(

                            RoleNames.RECEPTIONIST,
                            Set.of(
                                    PermissionCodes.CALENDAR_CREATE,
                                    PermissionCodes.CALENDAR_READ,
                                    PermissionCodes.CALENDAR_UPDATE,

                                    PermissionCodes.CLIENT_CREATE,
                                    PermissionCodes.CLIENT_READ,
                                    PermissionCodes.CLIENT_UPDATE
                            )
                    ),
                    Map.entry(

                            RoleNames.ACCOUNTING,
                            Set.of(
                                    PermissionCodes.BILLING_CREATE,
                                    PermissionCodes.BILLING_READ,
                                    PermissionCodes.BILLING_UPDATE,

                                    PermissionCodes.CLIENT_READ
                            )
                    ),
                    Map.entry(

                            RoleNames.BILLING_SPECIALIST,
                            Set.of(
                                    PermissionCodes.BILLING_CREATE,
                                    PermissionCodes.BILLING_READ,

                                    PermissionCodes.CLIENT_READ
                            )
                    ),
                    Map.entry(

                            RoleNames.CLIENT,
                            Set.of(
                                    PermissionCodes.DOCUMENT_DOWNLOAD
                            )
                    )
            );

}