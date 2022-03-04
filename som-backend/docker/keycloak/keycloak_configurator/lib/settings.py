from setuptools.package_index import Credential

from lib.utils.RealmRepresentation import RealmRepresentation

DEFAULT_SERVER_URL = "http://localhost:8080"
ADMIN_CREDS = Credential("admin", "123qwe")
SOM_REALM_NAME = "som_realm"
som_realm = RealmRepresentation(SOM_REALM_NAME)

SOM_CORE_SPRING_BOOT_ADMIN_CLIENT_ID = "som_core_spring_boot_admin"
SOM_CORE_USERS_CLIENT_ID = "som_core_users"

ADMIN_ROLE_NAME = "ADMIN"
USER_ROLE_NAME = "USER"
ADMIN_USER_NAME = "som_admin"
ADMIN_USER_PASSWORD = "123qwe"