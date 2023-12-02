ffmpeg -y -i 1.mp4 -i 2.mp4 -i 3.mp4 -filter_complex "[0:v][1:v]xfade=transition=fade:duration=1:offset=15.55,format=yuv420p[vfade1];[vfade1][2:v]xfade=transition=fade:duration=1:offset=32.55,format=yuv420p[vfade2];[0:a][1:a]acrossfade=d=1[afade1];[afade1][2:a]acrossfade=d=1[afade2]" -c:v h264_nvenc -b:v 60M -bf 2 -preset slow -c:a aac -map "[vfade2]" -map "[afade2]" -movflags +faststart out.mp4

