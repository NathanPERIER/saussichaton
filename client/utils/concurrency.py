
from threading import Lock


class MutexLock :

	def __init__(self) :
		self.mutex = Lock()
	
	def __enter__(self) -> "MutexLock" :
		self.mutex.acquire()
		return self
	
	def __exit__(self, exc_type, exc_value, exc_traceback) :
		self.mutex.release()
		if exc_type is not None :
			return False
