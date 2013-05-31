/*
 *  Esta Classe mostra a integracao com a camera usando a aplicacao nativa de camera do Android
 */

package lab.camera;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

/**
 * @author malves
 */
public class CameraOne extends Activity {

	/**
	 * Requisiçao para poder identificar quando a activity da camera e finalizada
	 */
	private static final int REQUEST_PICTURE = 1000;

	/**
	 * View onde a foto tirada sera exibida
	 */
	private ImageView imageView;
	
	/**
	 * Local de armazenamento da foto tirada 
	 */
	private File imageFile;

	/**
	 * Invocado quando a activity e criada
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_one);

		this.imageView = (ImageView) findViewById(R.id.imagem);
		
		// Obtem o local onde as fotos sao armazenadas na memoria externa do dispositivo
		File picsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		
		// Define o local completo onde a foto sera armazenada (diretorio + arquivo)
		this.imageFile = new File(picsDir, "fotoCameraOne.jpg");
	}

	/**
	 * Metodo que tira uma foto
	 * @param v
	 */
	public void takePicture(View v) {
		// Cria uma intent que sera usada para abrir a aplicaçao nativa de camera
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		// Indica na intent o local onde a foto tirada deve ser armazenada
		i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
		
		// Abre a aplicaçao de camera
		startActivityForResult(i, REQUEST_PICTURE);
	}

	/**
	 * Metodo chamado quando a aplicaçao nativa da camera e finalizada
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Verifica o codigo de requisiçao e se o resultado e OK (outro resultado indica que
		// o usuario cancelou a tirada da foto)
		if (requestCode == REQUEST_PICTURE && resultCode == RESULT_OK) {
			
			FileInputStream fis = null;
			
			try {
				try {
					// Cria um FileInputStream para ler a foto tirada pela camera
					fis = new FileInputStream(imageFile);
					
					// Converte a stream em um objeto Bitmap
					Bitmap picture = BitmapFactory.decodeStream(fis);
					
					// Exibe o bitmap na view, para que o usuario veja a foto tirada
					imageView.setImageBitmap(picture);
				} 
				finally {
					if (fis != null) 
						fis.close();					
				}
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}