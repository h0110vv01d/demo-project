import functools

from lib.connection.admin_session import get_admin_session, AdminSession


def clean_users(admin: AdminSession):
    delete_elements(admin.users.paged())


def clean_polls(admin: AdminSession):
    admin.polls.delete_many([poll.id for poll in admin.polls.paged()])


def delete_elements(elements):
    if elements is not None:
        for element in elements:
            element.delete()


def clean_before(cleanup_types: list):
    def wrapper(func):
        @functools.wraps(func)
        def inner(*args, **kwargs):
            admin = get_admin_session()
            check_cleanup_args(cleanup_types)
            for cleanup in cleanup_types:
                cleanup(admin)
            try:
                func(*args, **kwargs)
            finally:
                pass

        return inner

    return wrapper


def clean_after(cleanup_types: list):
    def wrapper(func):
        @functools.wraps(func)
        def inner(*args, **kwargs):
            try:
                func(*args, **kwargs)
            finally:
                admin = get_admin_session()
                check_cleanup_args(cleanup_types)
                for cleanup in cleanup_types:
                    cleanup(admin)

        return inner

    return wrapper


def check_cleanup_args(args):
    message = f"в cleanup нужно передавать список из CleanupType"
    if not isinstance(args, list):
        raise ValueError(message)
