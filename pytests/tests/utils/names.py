import os
from pathlib import Path

from lib.utils.credentials import Credentials

characters = """Sam Fisher
Jack Carver
Valerie Cortez
Carl Johnson
Thomas Vercetti
Arthas Menethil
Jaina Proudmoore
Elizabeth Caledonia
Jesse McCree
Olivia Colomar
Lena Oxton
Amélie Lacroix
Simon Jarrett
Terry Akers
Jin Yoshida
Catherine Chun
Johan Ross
Imogen Reed
Carl Semken
Raleigh Herber"""


def generate_creds():
    out = list()
    for name in characters.split('\n'):
        data = name.split(' ')
        first_name = data[0]
        last_name = data[1]
        login = f"{first_name.lower()}_{last_name.lower()}"
        creds = Credentials(login=login, first_name=first_name, last_name=last_name)
        out.append(creds)
    return out


users_with_names = generate_creds()

test_file = f"{Path(__file__).parent}/files/test.txt"
