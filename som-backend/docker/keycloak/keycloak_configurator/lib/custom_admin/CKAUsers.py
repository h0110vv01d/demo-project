import logging

from keycloak import KeycloakAdmin, KeycloakGetError

from lib.settings import ADMIN_USER_NAME, ADMIN_USER_PASSWORD, SOM_REALM_NAME, ADMIN_ROLE_NAME


class CKAUsers:
    def __init__(self, admin: KeycloakAdmin):
        self.admin = admin

    def is_admin_exist(self):
        try:
            resp = self.admin.get_users(query={"search": ADMIN_USER_NAME})
            if len(resp) == 0:
                return False
            else:
                logging.info(f"user {ADMIN_USER_NAME} already exists")
                return True
        except KeycloakGetError:
            return False

    def create_admin(self):
        if not self.is_admin_exist():
            logging.info(f"creating user {ADMIN_USER_NAME}")
            user_id = self.admin.create_user(payload={"enabled": True,
                                                      "attributes": {},
                                                      "groups": [],
                                                      "username": ADMIN_USER_NAME,
                                                      "emailVerified": ""})
            self.admin.set_user_password(user_id=user_id, password=ADMIN_USER_PASSWORD, temporary=False)
            # roles
            role_id = self.admin.get_realm_role(ADMIN_ROLE_NAME).get("id")
            self.admin.assign_realm_roles(user_id=user_id,
                                          roles=[{"id": role_id,
                                                  "name": ADMIN_ROLE_NAME,
                                                  "composite": False,
                                                  "clientRole": False,
                                                  "containerId": SOM_REALM_NAME}])
