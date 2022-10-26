
from threads import shared
from utils.sockets import Socket
from prompts.custom import prompt_types

import sys

def run(s: Socket) :
	while True :
		data = s.recv()
		if data['type'] == 'terminate' :
			return
		if data['type'] == 'disconnected' :
			print('Server disconnected abruptly')
			return
		if data['type'] == 'interrupt' :
			with shared.mutex :
				if shared.prompt is not None :
					shared.prompt = None
					s.interrupted()
			print()
		if data['type'] == 'ack' :
			print()
			if data['valid'] :
				with shared.mutex :
					shared.prompt = None
			else :
				print('Invalid input, please try again > ', end='')
				sys.stdout.flush()
		if data['type'] == 'message' :
			print(data['message'])
			print()
		if data['type'] in prompt_types :
			pt = prompt_types[data['type']]
			prompt = pt(data)
			with shared.mutex :
				shared.prompt = prompt
				shared.prompt.print()
			





