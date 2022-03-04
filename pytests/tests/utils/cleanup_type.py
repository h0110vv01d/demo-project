from enum import Enum

from tests.utils.fixtures import clean_users, clean_polls


class CleanupType(Enum):
    USERS = clean_users
    POLLS = clean_polls
