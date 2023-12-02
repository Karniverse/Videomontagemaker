(for %%i in (*.mp4) do @echo file '%%i') > list.txt
ffmpeg -y -f concat -safe 0 -i list.txt -c copy G:\output.mp4
del list.txt