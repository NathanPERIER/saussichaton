
from utils.concurrency import MutexLock
from prompts.base import Prompt


prompt: "Prompt | None" = None

mutex = MutexLock()
