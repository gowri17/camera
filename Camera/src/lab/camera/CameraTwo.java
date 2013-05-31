/*
 * Esta Classe mostra a integracao com a camera feita diretamente
 */

package lab.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

/**
 * @author malves
 */
public class CameraTwo extends Activity implements SurfaceHolder.Callback {

	/**
	 * Objeto que representa a camera
	 */
	private Camera camera;
	
	/**
	 * Local de armazenamento da foto tirada 
	 */
	private File imageFile;
	
	/**
	 * View para exibiçao do preview da imagem
	 */
	private SurfaceView surfaceView;

	/**
	 * Invocado quando a activity e criada
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_two);

		// Obtem uma instancia da camera
		camera = Camera.open();
		
		surfaceView = (SurfaceView) findViewById(R.id.preview);
		
		// Adiciona o callback à SurfaceView, para que a aplicaçao seja notificada quando
		// a superficia for criada, alterada ou destruida
		surfaceView.getHolder().addCallback(this);

		// Obtem o local onde as fotos sao armazenadas na memória externa do dispositivo
		File picsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		
		// Define o local completo onde a foto sera armazenada (diretório + arquivo)
		this.imageFile = new File(picsDir, "fotoCameraTwo.jpg");
	}

	/**
	 * Metodo que tira uma foto
	 * @param v
	 */
	public void takePicture(View v) {
		// Cria uma classe que implementa a interface PictureCallback, que sera usada como callback ao tirar uma foto
		PictureCallback jpeg = new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				FileOutputStream fos = null;
				try {
					try {
						// Grava os bytes da imagem no arquivo onde a foto deve ser armazenada
						fos = new FileOutputStream(imageFile);
						fos.write(data);
					} 
					finally {
						if (fos != null) 
							fos.close();
					}
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
				
				// Inicia novamente o preview
				camera.startPreview();
			}
		};

		// Tira uma foto. O callback fornecido e chamado assim que a imagem JPEG estiver disponivel
		camera.takePicture(null, null, jpeg);
		
		// Mostra uma mensagem indicando que a foto foi tirada
		Toast.makeText(this, "Foto gravada em " + imageFile.getPath(), Toast.LENGTH_LONG).show();
	}

	/**
	 * Invocado quando a activity esta prestes a ser destruida
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (camera != null) 
			// Libera o uso da camera
			camera.release();
	}

	/**
	 * Invocado quando a activity para de ser exibida na tela
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();

		if (camera != null) 
			// Para a visualizaçao do preview
			camera.stopPreview();
	}

	/**
	 * Chamado quando a superficie onde o preview e exibido e alterada
	 * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder, int, int, int)
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// Só atua se a superficie nao for nula
		if (holder.getSurface() != null) {
			
			try {
				// Para o preview
				camera.stopPreview();
			} 
			catch (Exception e) {
			}
			
			try {
				// Faz a ligaçao do preview da camera com a SurfaceView presente na tela
				camera.setPreviewDisplay(holder);
				
				// Inicia o preview
				camera.startPreview();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Chamado quando a superficie onde o preview e exibido e criada
	 * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder)
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			// Faz a ligaçao do preview da camera com a SurfaceView presente na tela
			camera.setPreviewDisplay(holder);
			
			// Inicia o preview
			camera.startPreview();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Chamado quando a superficie onde o preview e exibido e destruida
	 * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.SurfaceHolder)
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Nao e necessario fazer nada aqui
	}
}