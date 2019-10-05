package morales.acxel.spring.app.controllers;

import java.io.IOException;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import morales.acxel.spring.app.models.entity.Cliente;
import morales.acxel.spring.app.models.service.IClienteService;
import morales.acxel.spring.app.models.service.IUploadFileService;
import morales.acxel.spring.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUploadFileService uploadFileService;

	@RequestMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> viewPicture(@PathVariable String filename) {
		Resource resource = null;

		try {
			resource = this.uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model, @RequestParam(name = "page", defaultValue = "0") int page) {
		Pageable pageRequest = PageRequest.of(page, 5);
		Page<Cliente> clientes = this.clienteService.findAll(pageRequest);

		PageRender<Cliente> pageRender = new PageRender<>("/list", clientes);

		model.addAttribute("title", "Listado de Clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);

		return "list";
	}

	@GetMapping(value = "/form")
	public String crear(Model model) {
		Cliente cliente = new Cliente();

		model.addAttribute("title", "Crear Cliente");
		model.addAttribute("cliente", cliente);

		return "form";
	}

	@GetMapping(value = "/view/{id}")
	public String ver(@PathVariable("id") Long id, Model model, RedirectAttributes flash) {
		Cliente cliente = this.clienteService.findById(id);

		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la DB");
			return "redirect:/list";
		}

		model.addAttribute("cliente", cliente);
		model.addAttribute("title", "Detalle del Cliente: " + cliente.getNombre() + " " + cliente.getApellido());

		return "view";
	}

	@GetMapping(value = "/form/{id}")
	public String editar(Model model, @PathVariable(value = "id") Long id, RedirectAttributes flash) {
		Cliente cliente = null;

		if (id > 0) {
			cliente = this.clienteService.findById(id);

			if (cliente == null) {
				flash.addFlashAttribute("error", "No existe en la DB");
				return "redirect:/list";
			}
		} else {
			flash.addFlashAttribute("error", "El ID no puede ser cero");
			return "redirect:/list";
		}

		model.addAttribute("titulo", "Editar Cliente");
		model.addAttribute("cliente", cliente);

		return "form";
	}

	@PostMapping(value = "/form")
	public String guardar(Cliente cliente, SessionStatus session, RedirectAttributes flash, @RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {

			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null && cliente.getFoto().length() > 0) {
				this.uploadFileService.delete(cliente.getFoto());
			}
			
			String uniqueFilename = null;
			
			try {
				uniqueFilename = this.uploadFileService.copy(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			flash.addFlashAttribute("info", "Foto '" + uniqueFilename + "' subida correctamente");
			cliente.setFoto(uniqueFilename);
		}

		String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con éxito" : "Cliente creado con éxito";

		this.clienteService.save(cliente);

		session.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);

		return "redirect:list";
	}

	@RequestMapping(value = "/delete/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Cliente cliente = this.clienteService.findById(id);

			this.clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con éxito");

			if (this.uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con exito");
			}
		}

		return "redirect:/list";
	}

}
