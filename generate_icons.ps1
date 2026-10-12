$code = @"
using System;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Drawing.Imaging;

public class IconGen {
    public static void GenerateIcon(string path, int size, bool isRound) {
        using (Bitmap bmp = new Bitmap(size, size)) {
            using (Graphics g = Graphics.FromImage(bmp)) {
                g.SmoothingMode = SmoothingMode.AntiAlias;
                g.Clear(Color.Transparent);

                using (SolidBrush bgBrush = new SolidBrush(Color.FromArgb(255, 12, 14, 18))) {
                    if (isRound) {
                        g.FillEllipse(bgBrush, 0, 0, size, size);
                    } else {
                        g.FillRectangle(bgBrush, 0, 0, size, size);
                    }
                }

                using (SolidBrush greenBrush = new SolidBrush(Color.FromArgb(255, 139, 242, 0))) {
                    float scale = size / 109.0f;

                    g.FillEllipse(greenBrush, 28 * scale, 28 * scale, 53 * scale, 53 * scale);
                    using (SolidBrush bgInner = new SolidBrush(Color.FromArgb(255, 12, 14, 18))) {
                        g.FillEllipse(bgInner, 38 * scale, 38 * scale, 33 * scale, 33 * scale);
                        g.FillRectangle(bgInner, 50 * scale, 38 * scale, 35 * scale, 18 * scale);
                    }

                    g.FillRectangle(greenBrush, 48 * scale, 58 * scale, 28 * scale, 8 * scale);
                    g.FillRectangle(greenBrush, 48 * scale, 43 * scale, 28 * scale, 8 * scale);
                }
            }
            bmp.Save(path, ImageFormat.Png);
        }
    }
}
"@

Add-Type -TypeDefinition $code -ReferencedAssemblies System.Drawing

[IconGen]::GenerateIcon('C:\Users\USER\Documents\solargreen\android\app\src\main\ic_launcher-playstore.png', 512, $false)
[IconGen]::GenerateIcon('C:\Users\USER\Documents\solargreen\android\app\src\main\res\mipmap-mdpi\ic_launcher.png', 48, $false)
[IconGen]::GenerateIcon('C:\Users\USER\Documents\solargreen\android\app\src\main\res\mipmap-mdpi\ic_launcher_round.png', 48, $true)
[IconGen]::GenerateIcon('C:\Users\USER\Documents\solargreen\android\app\src\main\res\mipmap-hdpi\ic_launcher.png', 72, $false)
[IconGen]::GenerateIcon('C:\Users\USER\Documents\solargreen\android\app\src\main\res\mipmap-hdpi\ic_launcher_round.png', 72, $true)
[IconGen]::GenerateIcon('C:\Users\USER\Documents\solargreen\android\app\src\main\res\mipmap-xhdpi\ic_launcher.png', 96, $false)
[IconGen]::GenerateIcon('C:\Users\USER\Documents\solargreen\android\app\src\main\res\mipmap-xhdpi\ic_launcher_round.png', 96, $true)
[IconGen]::GenerateIcon('C:\Users\USER\Documents\solargreen\android\app\src\main\res\mipmap-xxhdpi\ic_launcher.png', 144, $false)
[IconGen]::GenerateIcon('C:\Users\USER\Documents\solargreen\android\app\src\main\res\mipmap-xxhdpi\ic_launcher_round.png', 144, $true)
[IconGen]::GenerateIcon('C:\Users\USER\Documents\solargreen\android\app\src\main\res\mipmap-xxxhdpi\ic_launcher.png', 192, $false)
[IconGen]::GenerateIcon('C:\Users\USER\Documents\solargreen\android\app\src\main\res\mipmap-xxxhdpi\ic_launcher_round.png', 192, $true)
