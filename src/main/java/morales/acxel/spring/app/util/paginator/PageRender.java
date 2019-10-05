package morales.acxel.spring.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;
	private Page<T> page;
	private int totalPaginas;
	private int numElementosPorPagina;
	private int paginaActual;
	private List<PageItem> paginas;

	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<PageItem>();

		this.numElementosPorPagina = 6;
		this.totalPaginas = page.getTotalPages();
		this.paginaActual = page.getNumber() + 1;

		int desde, hasta;
		if (this.totalPaginas <= this.numElementosPorPagina) {
			desde = 1;
			hasta = totalPaginas;
		} else {
			if (this.paginaActual <= (this.numElementosPorPagina / 2)) {
				desde = 1;
				hasta = this.numElementosPorPagina;
			} else if (this.paginaActual >= (this.totalPaginas - this.numElementosPorPagina / 2)) {
				desde = this.totalPaginas - (this.numElementosPorPagina + 1);
				hasta = this.numElementosPorPagina;
			} else {
				desde = this.paginaActual - (this.numElementosPorPagina / 2);
				hasta = this.numElementosPorPagina;
			}
		}

		for (int i = 0; i < hasta; i++) {
			paginas.add(new PageItem(desde + i, this.paginaActual == desde + i));
		}

	}

	public String getUrl() {
		return this.url;
	}

	public int getTotalPaginas() {
		return this.totalPaginas;
	}

	public int getPaginaActual() {
		return this.paginaActual;
	}

	public List<PageItem> getPaginas() {
		return this.paginas;
	}

	public boolean isFirst() {
		return this.page.isFirst();
	}

	public boolean isLast() {
		return this.page.isLast();
	}

	public boolean isHasNext() {
		return this.page.hasNext();
	}

	public boolean isHasPrevious() {
		return this.page.hasPrevious();
	}

}

