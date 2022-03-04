import logging

from lib.custom_admin.CKA import keycloak_admin

if __name__ == '__main__':
    logging.basicConfig(level=logging.INFO)
    keycloak_admin.configure()
    logging.info("end")
