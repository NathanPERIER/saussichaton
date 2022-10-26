#!/usr/bin/python3

from utils.sockets import Socket
from threads import reader, receiver

import re
import sys
from threading import Thread

def main(host: str, port: int) :
    with Socket('localhost', 6969) as s :
        reader_thread = Thread(target = lambda : reader.run(s), daemon=True)
        reader_thread.run()
        receiver.run(s)

def usage(retcode: int) :
    print(f"usage : {sys.argv[0]} -a <host> [-p <port>]")
    sys.exit(retcode)


if __name__ == '__main__' :

    args = sys.argv[1:]
    i = 0

    if len(args) > i and args[i] in ['-h', '--help'] :
        usage(0)
    
    if len(args) <= i+1 or args[i] != '-a' :
        usage(1)
    host = args[i+1]
    i += 2

    port = 6969
    if len(args) > i and args[i] == '-p' :
        i += 1
        if len(args) <= i :
            usage(1)
        port_str = args[i]
        if re.fullmatch(r'[0-9]+', port_str) is None :
            print(f"{port_str} is not a valid port")
            sys.exit(1)
        port = int(port_str)
    
    main(host, port)
