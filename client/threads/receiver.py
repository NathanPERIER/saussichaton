
from threads import shared
from utils.sockets import Socket
from utils.terminal import flush_input
from prompts.custom import prompt_types

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
				shared.prompt = None
				s.interrupted()
			print()
		if data['type'] == 'ack' :
			print()
			if data['valid'] :
				with shared.mutex :
					shared.prompt = None
			else :
				flush_input()
				print('Invalid input, please try again > ', end='')
		if data['type'] == 'message' :
			print(data['message'])
			print()
		if data['type'] in prompt_types :
			pt = prompt_types[data['type']]
			prompt = pt(data)
			flush_input()
			with shared.mutex :
				shared.prompt = prompt
				shared.prompt.print()
			





