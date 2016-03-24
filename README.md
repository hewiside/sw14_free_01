# Check it!

An Android app which enables you to play chess directly via WiFi (no internet needed!) on your mobile device. Check out this video to see how it's used:

[![Your browser doesn't support the video.](http://img.youtube.com/vi/hApHC8BfxiQ/0.jpg)](https://youtu.be/hApHC8BfxiQ)

The app knows virtually all chess rules including castling, check, check mate, and en passant. The only rule that's (partially) missing, is pawn conversion after it has reached the last rank (currently it is always converted into a queen).

Please note: The lags between a move and its appearance on the opponent's device is due to the socket timeout set to 3 seconds in the code (this time was chosen in order to facilitate testing (reduces logging)). The lags would vanish almost completely with a smaller socket timeout value - try it out and fork my repository : )
