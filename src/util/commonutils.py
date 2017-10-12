
def stringNotContainsChinese(string: str)->bool:
    if string is None:
        return False
    for i in range(len(string)):
        if ord(string[i]) > 255:
            return False
    return True
