import functools
from time import sleep


def repeat(attempts=8, k=0.1):
    def wrapper(func):
        @functools.wraps(func)
        def inner(*args, **kwargs):
            for attempt in range(1, attempts):
                try:
                    result = func(*args, **kwargs)
                    return result
                except (IndexError, KeyError, TimeoutError, ValueError, AssertionError):
                    sleep(attempt * k)
            return func(*args, **kwargs)

        return inner

    return wrapper
